package edu.tum.ase.compiler.integration;

import edu.tum.ase.compiler.CompilerService;
import edu.tum.ase.compiler.SourceCode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class CompilerServiceTest {

    @Autowired
    private CompilerService systemUnderTest;

    @Test
    public void should_ReturnIsCompilable_When_ValidJavaSourcecode() {
        // given
        String code = "public class App{public static void main(String[] args) {System.out.println(\"Hello World!\");}}";
        String filename = "App.java";
        String language = "Java";
        SourceCode sourceCode = new SourceCode(filename, language, code);

        // when
        SourceCode result = systemUnderTest.compile(sourceCode);

        // then
        assertTrue(result.isCompilable());
        assertEquals("", result.getStderr());
        assertEquals("", result.getStdout());
    }

    @Test
    public void should_ReturnIsCompilable_When_ValidCSourcecode() {
        // given
        String code = "#include<stdio.h> \n" +
                "int main() {\n" +
                " printf(\"Hello World\\n\");\n" +
                " return 0;\n" +
                "}";
        String filename = "my.c";
        String language = "C";
        SourceCode sourceCode = new SourceCode(filename, language, code);

        // when
        SourceCode result = systemUnderTest.compile(sourceCode);

        // then
        assertTrue(result.isCompilable());
        assertEquals("", result.getStderr());
        assertEquals("", result.getStdout());
    }

    @Test
    public void should_ReturnNotCompilable_When_InvalidSourcecode() {
        // given
        String code = "public class App{publi static void main(String[] args) {System.out.println(\"Hello World!\");}}";
        String filename = "App.java";
        String language = "Java";
        SourceCode sourceCode = new SourceCode(filename, language, code);

        // when
        SourceCode result = systemUnderTest.compile(sourceCode);

        // then
        assertFalse(result.isCompilable());
        assertNotEquals("", result.getStderr());
        assertEquals("", result.getStdout());
    }

    @TestConfiguration
    static class CompilerServiceTestsConfiguration {

        @Bean
        public CompilerService systemUnderTest() {
            return new CompilerService();
        }
    }

}
