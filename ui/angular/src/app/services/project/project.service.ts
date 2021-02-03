import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {BehaviorSubject, Observable} from 'rxjs';
import {IProject} from '@models/project.model';
import {environment} from '@env';
import {generateName} from '@utils/util';
import {getAuthHeaders} from '@services/auth/headers-util';
import {filter, map} from 'rxjs/operators';

const PROJECTS = environment.api.projects;

@Injectable({
  providedIn: 'root'
})
export class ProjectService {


  constructor(private http: HttpClient) {
    this.fetchAllProjects();
  }


  private _allProjects$: BehaviorSubject<IProject[]> = new BehaviorSubject<IProject[]>([]);

  get allProjects$(): BehaviorSubject<IProject[]> {
    return this._allProjects$;
  }

  public deleteProject(id: string): void {
    this.http.delete(`${PROJECTS}/${id}`, {headers: getAuthHeaders()})
      .subscribe((result: any) => {
        this.fetchAllProjects();
      });
  }

  public getProject(projectId: string): Observable<IProject> {
    return this.allProjects$.pipe(
      map((projects) => projects.find(p => p.id === projectId)),
    );
  }

  public createProject() {
    this.http.post(PROJECTS, {
      name: generateName()
    }, {headers: getAuthHeaders()})
      .subscribe((result: any) => {
        this.fetchAllProjects();
      });
  }

  public updateProject(project: IProject) {
    this.http.put(`${PROJECTS}/${project.id}`, {
      name: project.name
    }, {headers: getAuthHeaders()})
      .subscribe((result: any) => {
        this.fetchAllProjects();
      });
  }

  public addProjectMember(id: string, username: string) {
    this.http.post(`${PROJECTS}/${id}/members`, {username}, {headers: getAuthHeaders()})
      .subscribe((result: any) => {
        this.fetchAllProjects();
      });
  }

  public removeProjectMember(id: string, username: string) {
    this.http.delete(`${PROJECTS}/${id}/members/${username}`, {headers: getAuthHeaders()})
      .subscribe((result: any) => {
        this.fetchAllProjects();
      });
  }

  private fetchAllProjects(): void {
    this.http.get(PROJECTS, {headers: getAuthHeaders()})
      .subscribe((data: IProject | any) => {
        if (data?._embedded?.projectList) {
          this._allProjects$.next(data._embedded.projectList);
        } else {
          this._allProjects$.next([]);
        }
      });
  }
}
