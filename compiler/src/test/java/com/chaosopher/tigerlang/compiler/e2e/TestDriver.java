package com.chaosopher.tigerlang.compiler.e2e;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.chaosopher.tigerlang.compiler.main.Main;

import org.junit.Test;

public class TestDriver {

    @Test
    public void testTbiDesugar() throws InterruptedException, IOException {

        good("./src/test/java/com/chaosopher/tigerlang/compiler/fixtures/tbi-desguar.tig", new String[]{"--reg-alloc"});
    }


    @Test
    public void testTbiDesugarTerminate() throws InterruptedException, IOException {

        good("./src/test/java/com/chaosopher/tigerlang/compiler/fixtures/tbi-desguar-terminate.tig", new String[]{"--reg-alloc"});
    }

    public static String getFileContent(FileInputStream fis, String encoding) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(fis, encoding))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append('\n');
            }
            return sb.toString();
        }
    }


    public static String inputStreamToString(InputStream inputStream) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString("UTF-8");
    }

    public void good(String fileName,String args[]) throws InterruptedException, IOException {
        String baseName = fileName.substring(fileName.lastIndexOf("/") + 1);
        System.out.println("Testing program:" + baseName);
        InputStream runtimeAssemblyStream = getClass().getResourceAsStream("/runtimes/runtime.s");
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = runtimeAssemblyStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        String strResult = null;
        try (FileInputStream fin = new FileInputStream(fileName + ".result")) {
            strResult = getFileContent(fin, "UTF-8").trim();
        }
        String runtimeAssem = result.toString("UTF-8");
        try (FileInputStream fin = new FileInputStream(fileName)) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
            Main main = new Main(outputStream, errorStream);
            String[] both = Stream.concat(Arrays.stream(args), Arrays.stream(new String[] { fileName }))
                    .toArray(String[]::new);
            main.execute(both);
            System.out.println(new String(outputStream.toByteArray(), StandardCharsets.UTF_8));
            System.out.println(new String(errorStream.toByteArray(), StandardCharsets.UTF_8));
            Runtime runtime = Runtime.getRuntime();
            Process gccProcess = runtime.exec(
                    "gcc -x assembler-with-cpp -g -w -no-pie -Wimplicit-function-declaration -Wl,--wrap,getchar -o /tmp/"
                            + baseName + ".o -");
            BufferedWriter gccStdOut = new BufferedWriter(new OutputStreamWriter(gccProcess.getOutputStream()));
            gccStdOut.write(runtimeAssem);
            gccStdOut.write(outputStream.toString());
            gccStdOut.close();
            gccProcess.waitFor();
            // execute the binary that was created.
            Process compiledProcess = runtime.exec("/tmp/" + baseName + ".o");
            compiledProcess.waitFor();
            String actualResult = inputStreamToString(compiledProcess.getInputStream()).trim();
            System.out.println("Finished  --> " + baseName + " result:" + actualResult + " :exit" + compiledProcess.exitValue());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
