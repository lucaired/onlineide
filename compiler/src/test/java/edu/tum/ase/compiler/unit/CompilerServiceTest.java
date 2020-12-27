package edu.tum.ase.compiler.unit;

import edu.tum.ase.compiler.CompilerController;
import edu.tum.ase.compiler.CompilerService;
import edu.tum.ase.compiler.SourceCode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
//import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.Mockito.doNothing;

@RunWith(SpringRunner.class)
public class CompilerServiceTest {

    @Autowired
    private CompilerService systemUnderTest;

    // possible dependecies to mock: File, FileWriter, Runtime
    //TODO: mock Filewriter

    Runtime runtime = Mockito.mock(Runtime.class);

    Process p = Mockito.mock(Process.class);

    // fileWriter.write returns void therefore we don't need to mock it.
    FileWriter fileWriter = Mockito.mock(FileWriter.class);

    // TODO: File is still written to filesystem because this is done in the constructor -> Fix that

    public void mockRuntimeExec(String stdErr, String stdOut) throws IOException {
        InputStream errorStream = new ByteArrayInputStream(stdErr.getBytes());
        InputStream outputStream = new ByteArrayInputStream(stdOut.getBytes());

        given(p.getErrorStream()).willReturn(errorStream);
        given(p.getInputStream()).willReturn(outputStream);
        given(runtime.exec(anyString())).willReturn(p);
    }

    @Test
    public void should_ReturnIsCompilable_When_ValidSourcecodeObject() throws IOException {
        // given
        SourceCode sourceCode = new SourceCode();
        String code = "public class App{public static void main(String[] args) {System.out.println(\"Hello World!\");}}";
        String filename = "App.java";
        String language = "Java";
        sourceCode.setCode(code);
        sourceCode.setFileName(filename);
        sourceCode.setLanguage(language);
        mockRuntimeExec("", "");

        // when
        SourceCode result = systemUnderTest.compile(sourceCode);

        // then
        then(result.isCompilable());
        then(result.getStderr()).equals("");
        then(result.getStdout()).equals("");
        then(result.getCode()).equals(code);
        then(result.getFileName()).equals(filename);
        then(result.getLanguage()).equals(language);
    }

    @Test
    public void should_ReturnNotCompilable_When_NotValidSourcecodeObject() throws IOException {
        SourceCode sourceCode = new SourceCode();
        String code = "This is not valid c code.";
        String filename = "App.c";
        String language = "C";
        String errorMessage = "There is an error";
        sourceCode.setCode(code);
        sourceCode.setFileName(filename);
        sourceCode.setLanguage(language);
        mockRuntimeExec(errorMessage, "");

        // when
        SourceCode result = systemUnderTest.compile(sourceCode);

        // then
        then(!result.isCompilable());
        then(result.getStderr()).equals(errorMessage);
        then(result.getStdout()).equals("");
        then(result.getCode()).equals(code);
        then(result.getFileName()).equals(filename);
        then(result.getLanguage()).equals(language);
    }

    @Test
    public void should_ThrowException_When_LanguageIsInvalid() throws IOException {
        // given
        SourceCode sourceCode = new SourceCode();
        sourceCode.setFileName("App.py");
        sourceCode.setLanguage("python");
        sourceCode.setCode("code");
        mockRuntimeExec("", "");

        // when
        try{
            SourceCode result = systemUnderTest.compile(sourceCode);
            fail("Exception should be thrown by the function");
        } catch (Exception e){
            // then
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
    }

    @Test
    public void should_ReturnCompilable_When_StdErrContainsWhitespaces() throws IOException {
        // given
        SourceCode sourceCode = new SourceCode();
        String code = "public class App{public static void main(String[] args) {System.out.println(\"Hello World!\");}}";
        String filename = "App.java";
        String language = "Java";
        sourceCode.setCode(code);
        sourceCode.setFileName(filename);
        sourceCode.setLanguage(language);
        //TODO
        mockRuntimeExec("  g ", "");

        // when
        SourceCode result = systemUnderTest.compile(sourceCode);

        // then
        then(result.isCompilable());
        then(result.getStderr()).equals("");
        then(result.getStdout()).equals("");
        then(result.getCode()).equals(code);
        then(result.getFileName()).equals(filename);
        then(result.getLanguage()).equals(language);
    }


    @TestConfiguration
    static class CompilerServiceTestsConfiguration {

        @Bean
        public CompilerService systemUnderTest() {
            return new CompilerService();
        }
    }

    // TODO:


    // If Runtime returns only whitespaces we should assume that it was successful

}
