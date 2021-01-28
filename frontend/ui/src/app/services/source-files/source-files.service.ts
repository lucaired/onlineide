import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '@env';
import {ISourceFile} from '@models/source-file.model';
import {map} from 'rxjs/operators';

const PROJECTS = environment.api.projects;

@Injectable({
  providedIn: 'root'
})
export class SourceFilesService {
  constructor(private http: HttpClient) {
  }

  public getSourceFiles(projectId: string): Observable<any[] | any> {
    return this.http.get(`${PROJECTS}/${projectId}/sourcefiles`);
  }

  public createSourceFile(projectId: string, sourceFile: ISourceFile): Observable<ISourceFile | any> {
    return this.http.post(`${PROJECTS}/${projectId}/sourcefiles`, sourceFile);
  }

  public replaceSourceFile(projectId: string, sourceFile: ISourceFile): Observable<ISourceFile | any> {
    return this.http.put(`${PROJECTS}/${projectId}/sourcefiles/${sourceFile.id}`, sourceFile);
  }

  public deleteSourceFile(projectId: string, sourceFileId: string): Observable<any> {
    return this.http.delete(`${PROJECTS}/${projectId}/sourcefiles/${sourceFileId}`);
  }
}
