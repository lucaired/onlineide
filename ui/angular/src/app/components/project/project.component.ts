import {Component, OnInit} from '@angular/core';
import {ProjectService} from '@services/project/project.service';
import {IProject} from '@models/project.model';
import {Router} from '@angular/router';
import {DarkModeService} from '@services/dark-mode/dark-mode.service';
import {AuthService} from '@services/auth/auth.service';

@Component({
  selector: 'app-project',
  templateUrl: './project.component.html',
  styleUrls: ['./project.component.scss']
})
export class ProjectComponent implements OnInit {
  isVisible = false;
  currentProject?: IProject = null;

  constructor(
    public projectService: ProjectService,
    private router: Router,
    public darkModeService: DarkModeService,
    public authService: AuthService,
  ) {
  }

  ngOnInit(): void {
  }

  addProject() {
    this.projectService.createProject();
  }


  openProject(id: string) {
    this.router.navigate([`/projects/${id}/edit`]);
  }

  deleteProject(id: string) {
    this.projectService.deleteProject(id);
  }

  showModalForProject(project: IProject): void {
    this.currentProject = project;
    this.isVisible = true;
  }

  handleOk(): void {
    this.isVisible = false;
    this.projectService.updateProject(this.currentProject);
  }

  handleCancel(): void {
    // do-nothing-democrats
    this.isVisible = false;
  }
}
