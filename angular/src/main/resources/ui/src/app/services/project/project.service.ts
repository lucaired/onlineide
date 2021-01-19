import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {BehaviorSubject} from 'rxjs';
import {IProject} from '@models/project.model';
import {environment} from '@env';
import {generateName} from '@utils/util';

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
    this.http.delete(`${PROJECTS}/${id}`)
      .subscribe((result: any) => {
        this.fetchAllProjects();
      });
  }

  public createProject() {
    this.http.post(PROJECTS, {
      name: generateName()
    })
      .subscribe((result: any) => {
        this.fetchAllProjects();
      });
  }

  public updateProject(project: IProject) {
    this.http.put(`${PROJECTS}/${project.id}`, {
      name: project.name
    })
      .subscribe((result: any) => {
        this.fetchAllProjects();
      });
  }

  private fetchAllProjects(): void {
    this.http.get(PROJECTS)
      .subscribe((data: IProject | any) => {
        if (data?._embedded?.projectList) {
          this._allProjects$.next(data._embedded.projectList);
        } else {
          this._allProjects$.next([]);
        }
      });
  }
}
