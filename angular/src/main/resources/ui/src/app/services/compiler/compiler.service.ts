import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '@env';
import {ISourceFile} from '@models/source-file.model';
import {Observable} from 'rxjs';
import {getFileExtension} from '@utils/util';
import {ICompilableFile, ProgrammingLanguage} from '@models/compilable-file.model';

const COMPILE = environment.api.compiler;

@Injectable({
  providedIn: 'root'
})
export class CompilerService {

  constructor(private http: HttpClient) {
  }

  public compile(sourceFile: ISourceFile): Observable<ICompilableFile | any> {
    const fileExtension = getFileExtension(sourceFile.fileName);
    const compilableFile: ICompilableFile = {
      code: sourceFile.sourceCode,
      fileName: sourceFile.fileName,
      language: fileExtension === 'c' ? ProgrammingLanguage.C : ProgrammingLanguage.JAVA,
    };
    return this.http.post(COMPILE, compilableFile);
  }
}
