package com.chaosopher.tigerlang.compiler.e2e;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    @Theory
    public void good(String fileName) throws InterruptedException {
        System.out.println("Testing Compiler" + fileName);
        ErrorMsg errorMsg = new ErrorMsg("f", System.out);
        ParserService parserService = new ParserService(new ParserFactory());
        parserService.configure(config -> config.setNoPrelude(false));
        parserService.configure(config -> config.setParserTrace(false));
        try (FileInputStream fin = new FileInputStream(fileName)) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
            Main main = new Main(outputStream, errorStream);
            main.execute(new String[]{fileName});
            System.out.println(new String(outputStream.toByteArray(), StandardCharsets.UTF_8));
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec(
                    "gcc -x assembler-with-cpp -g -w -no-pie -Wimplicit-function-declaration -Wl,--wrap,getchar -");
            // get the streams for the gcc process.
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            BufferedWriter stdOut = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
            // write runtime assembly
            String runtimeAssem = "";
            stdOut.write(runtimeAssem);
            //write tiger generated assemebly
            stdOut.write(outputStream.toString());
     //       stdOut.write("int main() {}");
       //     stdOut.write(".global tigermain");
         //   stdOut.write((char)26);
        //    stdOut.write((char)13);
            stdOut.close();
            process.waitFor();

            // Read the output from the command
            System.out.println("Here is the standard output of the command:\n");
            String s = null;
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }

            // Read any errors from the attempted command
            System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }

            System.out.println(" --> " + process.exitValue());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
