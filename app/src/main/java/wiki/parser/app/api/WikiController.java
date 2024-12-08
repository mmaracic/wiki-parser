package wiki.parser.app.api;

import lombok.RequiredArgsConstructor;
import org.apache.commons.compress.compressors.CompressorException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wiki.parser.app.service.WikiFileProcessor;
import wiki.parser.core.model.WikiPage;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("wiki")
@RestController
public class WikiController {

    private final WikiFileProcessor wikiFileProcessor;

    @GetMapping(path = "search", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<WikiPage> findWiki(@RequestParam(name = "title-text") String searchTitleText) throws XMLStreamException, CompressorException, IOException {
        String indexFileName = "../data/simplewiki-20240901-pages-articles-multistream-index.txt.bz2";
        String wikiFileName = "../data/simplewiki-20240901-pages-articles-multistream.xml.bz2";

        return wikiFileProcessor.process(wikiFileName, indexFileName, true, searchTitleText);
    }


}
