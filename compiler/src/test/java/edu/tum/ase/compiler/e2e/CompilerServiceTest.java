package edu.tum.ase.compiler.e2e;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tum.ase.compiler.SourceCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// The @AutoConfigureMockMvc annotation is important for E2E testing:
// That way, almost of the full stack is used, so that the code will be called in exactly the same way as if it were processing a real HTTP request but without the cost of starting the server.
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CompilerServiceTest {

    private final String URL = "/api/compile/";

    @Autowired
    private MockMvc systemUnderTest;

    @Autowired
    private ObjectMapper objectMapper;

    private SourceCode createValidJavaSourceCode() {
        String code = "public class App{public static void main(String[] args) {System.out.println(\"Hello World!\");}}";
        String filename = "App.java";
        String language = "Java";
        return new SourceCode(filename, language, code);
    }

    private SourceCode createInvalidJavaSourceCode() {
        String code = "public class App{oeffentlich static void main(String[] args) {System.out.println(\"Hello World!\");}}";
        String filename = "App.java";
        String language = "Java";
        return new SourceCode(filename, language, code);
    }

    @Test
    public void should_ReturnIsCompilable_When_ValidSourcecodeObject() throws Exception {
        // given
        SourceCode sourceCode = createValidJavaSourceCode();

        // when
        ResultActions result = systemUnderTest.perform(post(URL)
                .content(objectMapper.writeValueAsString(sourceCode))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(sourceCode.getCode()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fileName").value(sourceCode.getFileName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.language").value(sourceCode.getLanguage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.stderr").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.compilable").value(true));
    }

    @Test
    public void should_ReturnNotCompilable_When_InvalidSourcecode() throws Exception {
        // given
        SourceCode sourceCode = createInvalidJavaSourceCode();

        // when
        ResultActions result = systemUnderTest.perform(post(URL)
                .content(objectMapper.writeValueAsString(sourceCode))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(sourceCode.getCode()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fileName").value(sourceCode.getFileName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.language").value(sourceCode.getLanguage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.stderr").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.compilable").value(false));
    }
}
