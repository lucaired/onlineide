package edu.tum.ase.compiler.unit;

import edu.tum.ase.compiler.CompilerService;
import edu.tum.ase.compiler.SourceCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class CompilerServiceTest {

    @Autowired
    private CompilerService systemUnderTest;

    private void stubRuntimeExec(String stdErr) throws IOException {
        InputStream errorStream = new ByteArrayInputStream(stdErr.getBytes());
        InputStream outputStream = new ByteArrayInputStream("".getBytes());

        Process processMock = Mockito.mock(Process.class);
        given(processMock.getErrorStream()).willReturn(errorStream);
        given(processMock.getInputStream()).willReturn(outputStream);

        Runtime runtimeMock = Mockito.mock(Runtime.class);
        given(runtimeMock.exec(Mockito.any(String[].class))).willReturn(processMock);

        // This specifies that in the class under test the created mock is used as Runtime object.
        // Its exec() method in turn returns a mock for Process that reflects the behavior to be tested (see above).
        given(systemUnderTest.getRuntime()).willReturn(runtimeMock);
    }

    private void stubFileWriter() throws IOException {
        FileWriter writerMock = Mockito.mock(FileWriter.class);
        // This stub definition is not explicitly necessary because mocks use doNothing() for void methods by default. However, it makes our intention clearer.
        doNothing().when(writerMock).write(anyString());

        // In this case it is necessary to use the doReturn() notation (unlike, for example, given() or when()).
        // Otherwise a NullPointerException may occur because the stubbed method is initially called with a null object.
        doReturn(writerMock).when(systemUnderTest).makeWriter(any(File.class));
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

    @BeforeEach
    public void init() throws IOException {
        stubFileWriter();
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
    public void should_ReturnIsCompilable_When_StdErrContainsWhitespaces() throws IOException {
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

        // when + then
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> systemUnderTest.compile(sourceCode));
        assertEquals("'python' is not supported", exception.getMessage());
    }

    @Test
    public void should_ThrowException_When_TempFileNotWritable() throws IOException {
        // given
        SourceCode sourceCode = createValidJavaSourceCode();
        FileWriter writerMock = Mockito.mock(FileWriter.class);
        doThrow(IOException.class).when(writerMock).write(anyString());
        doReturn(writerMock).when(systemUnderTest).makeWriter(any(File.class));

        // when + then
        Throwable exception = assertThrows(RuntimeException.class, () -> systemUnderTest.compile(sourceCode));
        assertTrue(exception.getMessage().startsWith("Could not write temporary file used for compilation"));
    }


    @Test
    public void should_ThrowException_When_CmdExecutionFailed() throws IOException {
        // given
        SourceCode sourceCode = createValidJavaSourceCode();
        Runtime runtimeMock = mock(Runtime.class);
        given(runtimeMock.exec(Mockito.any(String[].class))).willThrow(IOException.class);
        given(systemUnderTest.getRuntime()).willReturn(runtimeMock);

        // when + then
        Throwable exception = assertThrows(RuntimeException.class, () -> systemUnderTest.compile(sourceCode));
        assertTrue(exception.getMessage().startsWith("Could not execute the compilation command"));
    }

    @TestConfiguration
    static class CompilerServiceTestsConfiguration {

        @Bean
        public CompilerService systemUnderTest() {
            // Create a Mockito spy from the actual object to be able to stub certain methods.
            // Spies call the real methods of the used object instance by default, as long as they have not been explicitly
            // overridden with stubs (see https://www.baeldung.com/mockito-spy#mock-vs-spy-in-mockito).
            return Mockito.spy(new CompilerService());
        }
    }
}
