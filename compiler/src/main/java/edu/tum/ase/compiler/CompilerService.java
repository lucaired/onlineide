package edu.tum.ase.compiler;

import com.google.common.io.Files;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

@Service
public class CompilerService {

    private static String convertToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    private String getCompileCommand(String language, String path) {
        switch (language) {
            case "Java":
                return "javac " + path;
            case "C":
                return "gcc -c " + path;
            default:
                throw new IllegalArgumentException("'" + language + "' is not supported");
        }
    }

    public Runtime getRuntime() {
        return Runtime.getRuntime();
    }

    public FileWriter makeWriter(File f) throws IOException {
        return new FileWriter(f);
    }

    public SourceCode compile(SourceCode sourceCode) {

        File file = new File(Files.createTempDir(), sourceCode.getFileName());
        try {
            FileWriter writer = makeWriter(file);
            writer.write(sourceCode.getCode());
            writer.close();
        } catch (IOException ex) {
            throw new RuntimeException("Could not write temporary file used for compilation: " + file.getPath());
        }

        String cmd = getCompileCommand(sourceCode.getLanguage(), file.getPath());
        Process p;
        try {
            p = getRuntime().exec(cmd);
        } catch (IOException e) {
            throw new RuntimeException("Could not execute the compilation command: " + cmd);
        }

        String stderr = convertToString(p.getErrorStream());
        sourceCode.setStderr(stderr);
        String stdout = convertToString(p.getInputStream());
        sourceCode.setStdout(stdout);

        sourceCode.setCompilable(stderr.isBlank());

        // TODO: Delete the temporary file (and parent folder) after the compilation?

        return sourceCode;
    }

}
