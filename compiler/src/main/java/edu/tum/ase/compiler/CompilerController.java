package edu.tum.ase.compiler;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class CompilerController {

    @Autowired
    private CompilerService compilerService;

    @RequestMapping(path = "/compile", method = RequestMethod.POST)
    public SourceCode compile(@Valid @RequestBody SourceCode sourceCode) {
        return compilerService.compile(sourceCode);
    }
}