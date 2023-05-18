package subway.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.MockMvc;
import subway.application.LineService;
import subway.application.SectionService;
import subway.application.path.PathService;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

@WebMvcTest(controllers = {PathController.class, LineController.class, SectionController.class})
@AutoConfigureRestDocs
public abstract class DocumentationSteps {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected LineService lineService;

    @MockBean
    protected SectionService sectionService;

    @MockBean
    protected PathService pathService;

    protected static RestDocumentationResultHandler document(String path) {
        return MockMvcRestDocumentation.document(path,
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()));
    }
}