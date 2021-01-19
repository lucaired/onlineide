package edu.tum.ase.compiler;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin
public class CompilerController {

    @Autowired
    private CompilerService compilerService;

    @RequestMapping(path = "/api/compile", method = RequestMethod.POST)
    public SourceCode compile(@Valid @RequestBody SourceCode sourceCode) {
        return compilerService.compile(sourceCode);
    }
}