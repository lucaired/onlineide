export interface ISourceFile {
  id?: string;
  fileName: string;
  sourceCode: string;

  selected?: boolean;
  dirty?: boolean;
}
