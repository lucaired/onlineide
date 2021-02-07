import {ISourceFile} from './source-file.model';

export interface IProject {
  id?: string;
  name: string;
  memberIds: string[];
  // sourceFiles: ISourceFile[]
}
