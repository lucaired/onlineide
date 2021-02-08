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

        File tempDir = Files.createTempDir();
        File file = new File(tempDir, sourceCode.getFileName());
        try {
            FileWriter writer = makeWriter(file);
            writer.write(sourceCode.getCode());
            writer.close();
        } catch (IOException ex) {
            throw new RuntimeException("Could not write temporary file used for compilation: " + file.getPath());
        }

        // Build a shell command not only consisting of the actual compiler command, but change into the created temporary directory first.
        // This is necessary (at least for gcc) so that the compiled file is not created in the current working directory, but in the temporary folder.
        String cmd = "cd " + tempDir + " && " + getCompileCommand(sourceCode.getLanguage(), sourceCode.getFileName());
        Process p;
        try {
            // Since several commands are concatenated (&&) this explicit command structure is necessary when calling exec().
            String[] commands = {"/bin/bash", "-c", cmd};
            p = getRuntime().exec(commands);
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
