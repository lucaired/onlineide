export interface ICompilableFile {
  code: string;
  fileName: string;
  language: ProgrammingLanguage;

  stdout?: string;
  stderr?: string;
  compilable?: boolean;
}

export enum ProgrammingLanguage {
  JAVA = 'Java',
  C = 'C',
}
