package com.chaosopher.tigerlang.compiler.e2e;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import com.chaosopher.tigerlang.compiler.absyn.Absyn;
import com.chaosopher.tigerlang.compiler.absyn.PrettyPrinter;
import com.chaosopher.tigerlang.compiler.errormsg.ErrorMsg;
import com.chaosopher.tigerlang.compiler.main.Main;
import com.chaosopher.tigerlang.compiler.parse.ParserFactory;
import com.chaosopher.tigerlang.compiler.parse.ParserService;

@RunWith(Theories.class)
public class RegTest {

    @DataPoints
    public static String[] paths() {
        try (Stream<Path> walk = Files.walk(Paths.get("./src/test/java/com/chaosopher/tigerlang/compiler/fixtures/"))) {
            return walk.filter(Files::isRegularFile).map(x -> x.toString()).filter(f -> f.endsWith(".tig"))
                    .collect(Collectors.toList()).toArray(String[]::new);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void dumpOutputForProcess(Process gccProcess) throws IOException {
        BufferedReader gccStdInput = new BufferedReader(new InputStreamReader(gccProcess.getInputStream()));
        BufferedReader gccStdError = new BufferedReader(new InputStreamReader(gccProcess.getErrorStream()));
        // Read the output from the command
        System.out.println("Here is the standard output of the command:\n");
        String s = null;
        while ((s = gccStdInput.readLine()) != null) {
            System.out.println(s);
        }
        // Read any errors from the attempted command
        System.out.println("Here is the standard error of the command (if any):\n");
        while ((s = gccStdError.readLine()) != null) {
            System.out.println(s);
        }
    }

    @Theory
    public void good(String fileName) throws InterruptedException, IOException {
        String baseName = fileName.substring(fileName.lastIndexOf("/") + 1);
        System.out.println("Testing program:" + baseName);
        ErrorMsg errorMsg = new ErrorMsg("f", System.out);
        ParserService parserService = new ParserService(new ParserFactory());
        parserService.configure(config -> config.setNoPrelude(false));
        parserService.configure(config -> config.setParserTrace(false));
        InputStream runtimeAssemblyStream = getClass().getResourceAsStream("/runtimes/runtime.s");
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = runtimeAssemblyStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        String runtimeAssem = result.toString("UTF-8");
        try (FileInputStream fin = new FileInputStream(fileName)) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
            Main main = new Main(outputStream, errorStream);
            main.execute(new String[]{fileName});
            System.out.println(new String(outputStream.toByteArray(), StandardCharsets.UTF_8));
            Runtime runtime = Runtime.getRuntime();
            Process gccProcess = runtime.exec(
                    "gcc -x assembler-with-cpp -g -w -no-pie -Wimplicit-function-declaration -Wl,--wrap,getchar -o /tmp/" + baseName +".o -");
            BufferedWriter gccStdOut = new BufferedWriter(new OutputStreamWriter(gccProcess.getOutputStream()));
            gccStdOut.write(runtimeAssem);
            gccStdOut.write(outputStream.toString());
            gccStdOut.close();
            gccProcess.waitFor();
            dumpOutputForProcess(gccProcess);
            assertEquals(gccProcess.exitValue(), 0);
            System.out.println(" --> " + baseName + ":" + gccProcess.exitValue());

            //execute the binary that was created.
            Process compiledProcess = runtime.exec(
                    "/tmp/" + baseName +".o");
            compiledProcess.waitFor();
            dumpOutputForProcess(compiledProcess);
            System.out.println(" --> " + baseName + ":" + compiledProcess.exitValue());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
