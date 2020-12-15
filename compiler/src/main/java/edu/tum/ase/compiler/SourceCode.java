package edu.tum.ase.compiler;

import edu.tum.ase.validators.ValueOfEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

enum Language {
    Java,
    C,
}

@Getter
@Setter
@NoArgsConstructor
public class SourceCode {

    @NotBlank
    private String code;

    @NotBlank
    private String fileName;

    @ValueOfEnum(enumClass = Language.class)
    private String language;

    private String stdout;
    private String stderr;
    private boolean compilable = false;
}
