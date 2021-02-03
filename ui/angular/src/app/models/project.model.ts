import {ISourceFile} from './source-file.model';

export interface IProject {
  id?: string;
  name: string;
  membersIds: string[];
  // sourceFiles: ISourceFile[]
}
