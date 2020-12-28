package edu.tum.ase.compiler.unit;

import edu.tum.ase.compiler.CompilerService;
import edu.tum.ase.compiler.SourceCode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
public class CompilerServiceTest {

    Runtime runtime = Mockito.mock(Runtime.class);

    Process p = Mockito.mock(Process.class);

    // TODO: File is still written to filesystem because this is done in the constructor -> Fix that
    FileWriter fileWriter = Mockito.mock(FileWriter.class);

    @Autowired
    private CompilerService systemUnderTest;

    public void stubRuntimeExec(String stdErr) throws IOException {
        InputStream errorStream = new ByteArrayInputStream(stdErr.getBytes());
        InputStream outputStream = new ByteArrayInputStream("".getBytes());

        given(p.getErrorStream()).willReturn(errorStream);
        given(p.getInputStream()).willReturn(outputStream);
        given(runtime.exec(anyString())).willReturn(p);
    }

    /**
     * Utility function to generate a {@link SourceCode}
     *
     * @return
     */
    private SourceCode createValidJavaSourceCode() {
        String code = "public class App{public static void main(String[] args) {System.out.println(\"Hello World!\");}}";
        String filename = "App.java";
        String language = "Java";
        return new SourceCode(filename, language, code);
    }

    /**
     * Utility function to compare the attribute values of {@link SourceCode} objects that are set before running compiler functions.
     *
     * @param a - First {@link SourceCode} object
     * @param b - Second {@link SourceCode} object
     */
    private void assertAttributesAreUnchanged(SourceCode a, SourceCode b) {
        boolean unchanged = a.getCode().equals(b.getCode()) &&
                a.getFileName().equals(b.getFileName()) &&
                a.getLanguage().equals(b.getLanguage());
        assertTrue(unchanged);
    }

    @Test
    public void should_ReturnIsCompilable_When_ValidSourcecodeObject() throws IOException {
        // given
        SourceCode sourceCode = createValidJavaSourceCode();
        stubRuntimeExec("");

        // when
        SourceCode result = systemUnderTest.compile(sourceCode);

        // then
        assertTrue(result.isCompilable());
        assertEquals("", result.getStderr());
        assertEquals("", result.getStdout());
        assertAttributesAreUnchanged(result, sourceCode);
    }

    @Test
    public void should_ReturnNotCompilable_When_NotValidSourcecodeObject() throws IOException {
        // given
        String code = "This is not valid C code.";
        String filename = "App.c";
        String language = "C";
        String errorMessage = "There is an error";
        SourceCode sourceCode = new SourceCode(filename, language, code);
        stubRuntimeExec(errorMessage);

        // when
        SourceCode result = systemUnderTest.compile(sourceCode);

        // then
        assertFalse(result.isCompilable());
        assertEquals(errorMessage, result.getStderr());
        assertEquals("", result.getStdout());
        assertAttributesAreUnchanged(result, sourceCode);
    }

    @Test
    public void should_ThrowException_When_LanguageIsInvalid() throws IOException {
        // given
        SourceCode sourceCode = new SourceCode("App.py", "python", "code");
        stubRuntimeExec("");

        // when
        try {
            systemUnderTest.compile(sourceCode);
            fail("Exception should be thrown by the function");
        } catch (Exception e) {
            // then
            assertThat(e, instanceOf(IllegalArgumentException.class));
            assertEquals("'python' is not supported", e.getMessage());
        }
    }

    @Test
    public void should_ReturnCompilable_When_StdErrContainsWhitespaces() throws IOException {
        // given
        SourceCode sourceCode = createValidJavaSourceCode();
        stubRuntimeExec("   ");

        // when
        SourceCode result = systemUnderTest.compile(sourceCode);

        // then
        assertTrue(result.isCompilable());
        assertEquals("   ", result.getStderr());
        assertEquals("", result.getStdout());
        assertAttributesAreUnchanged(result, sourceCode);
    }


    @TestConfiguration
    static class CompilerServiceTestsConfiguration {

        @Bean
        public CompilerService systemUnderTest() {
            return new CompilerService();
        }
    }
}
