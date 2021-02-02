import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {catchError, map, mergeMap} from 'rxjs/operators';
import {SourceFilesService} from '@services/source-files/source-files.service';
import {ISourceFile} from '@models/source-file.model';
import {BehaviorSubject, Observable, of} from 'rxjs';
import {DarkModeService} from '@services/dark-mode/dark-mode.service';
import {getFileExtension} from '@utils/util';
import {ProjectService} from '@services/project/project.service';
import {CompilerService} from '@services/compiler/compiler.service';
import {ICompilableFile} from '@models/compilable-file.model';
import {NzMessageService} from 'ng-zorro-antd/message';
import {Theme} from '@models/theme.model';
import {IDarkMode} from '@models/dark-mode.model';


@Component({
  selector: 'app-ide',
  templateUrl: './ide.component.html',
  styleUrls: ['./ide.component.scss']
})
export class IdeComponent implements OnInit {
  editorOptions$ = new BehaviorSubject({theme: 'vs-light', language: 'java'});

  sourceFiles$: BehaviorSubject<ISourceFile[]> = new BehaviorSubject<ISourceFile[]>([]);

  selectedSourceFile$: BehaviorSubject<ISourceFile> = new BehaviorSubject<ISourceFile>(null);

  currentCompilationResult$ = new BehaviorSubject<ICompilableFile>(null);

  newFileModalVisible = false;

  // TODO: Improve/remove for real implementation
  projectMemberToEdit: string; // currently used to remove and add members

  newSourceFile: ISourceFile = {fileName: '', sourceCode: ''};
  fileAlreadyExists = false;
  projectId$ = new BehaviorSubject<string>(null);

  private theme$: BehaviorSubject<Theme> = new BehaviorSubject<Theme>(Theme.LIGHT);


  constructor(
    private activatedRoute: ActivatedRoute,
    public sourceFilesService: SourceFilesService,
    public compilerService: CompilerService,
    public darkModeService: DarkModeService,
    public projectService: ProjectService,
    private messageService: NzMessageService
  ) {
  }

  ngOnInit(): void {
    this.activatedRoute.paramMap.pipe(
      mergeMap(paramMap => {
        const id: string = paramMap.get('id');
        this.projectId$.next(id);
        return this.sourceFilesService.getSourceFiles(id).pipe(
          map((sourceFiles) => {
            // Set first source file as default
            this.selectedSourceFile$.next(sourceFiles[0]);
            return sourceFiles;
          }));
      }),
      catchError(err => {
        console.error(err);
        return of(null);
      })
    ).subscribe(sourceFiles => {
      this.sourceFiles$.next(sourceFiles);
    });

    // Dark mode polling
    setInterval(() => {
      try {
        this.darkModeService.getDarkMode().subscribe((res: IDarkMode) => {
          if (res?.enabled) {
            this.theme$.next(Theme.DARK);
          } else {
            this.theme$.next(Theme.LIGHT);
          }
        });
      } catch (err) {

      }
    }, 3000);

    // Toggle editor theme
    this.theme$.subscribe(theme => {
      if (theme) {
        const options = this.editorOptions$.getValue();
        options.theme = `vs-${theme}`;
        this.editorOptions$.next(options);
      }
    });

    // Toggle editor language
    this.selectedSourceFile$.subscribe(file => {
      if (file) {
        const options = this.editorOptions$.getValue();
        options.language = getFileExtension(file.fileName);
        this.editorOptions$.next(options);
      }
    });
  }

  addMember(): void {
    const projectId = this.projectId$.getValue();
    this.projectService.addProjectMember(projectId, this.projectMemberToEdit);
  }

  removeMember(): void {
    const projectId = this.projectId$.getValue();
    this.projectService.removeProjectMember(projectId, this.projectMemberToEdit);
  }

  setDirty(sourceFile: ISourceFile): void {
    sourceFile.dirty = true;
  }

  saveSourceFile(sourceFile: ISourceFile): void {
    this.sourceFilesService.replaceSourceFile(this.projectId$.getValue(), sourceFile).subscribe(res => {
      this.messageService.success('File saved');
      sourceFile.dirty = false;
      // console.log(res);
    });
  }

  compileSourceFile(sourceFile: ISourceFile): void {
    // `selectedSourceFile$` will contain the latest server state, we're checking the sourcecode against
    // the current file passed for compilation. If the contents don't match we ask the user to save the file
    // first
      if (sourceFile.dirty) {
        this.messageService.error('Save file first');
      } else {
        this.compilerService.compile(sourceFile).subscribe((res: ICompilableFile) => {
          console.log(res);
          if (res?.fileName) {
            this.currentCompilationResult$.next(res);
            if (res.compilable) {
              this.messageService.success('Compilation successful 😍');
            } else {
              this.messageService.error('Compilation failed.');
            }
          } else {
            this.messageService.error('Unknown error :/');
          }
        });
      }
  }

  deleteSourceFile(sourceFileId: string): void {
    this.sourceFilesService.deleteSourceFile(this.projectId$.getValue(), sourceFileId).subscribe(res => {
      this.fetchSourceFiles();
    });
  }

  // New file modal
  openNewFileModal(): void {
    this.newSourceFile = {fileName: '', sourceCode: ''};
    this.newFileModalVisible = true;
  }

  handleOk(): void {
    const alreadyExists = this.sourceFiles$.getValue().findIndex(f => f.fileName === this.newSourceFile.fileName) > -1;
    if (!alreadyExists) {
      this.sourceFilesService.createSourceFile(this.projectId$.getValue(), this.newSourceFile).subscribe((res) => {
        this.fetchSourceFiles();
        this.newFileModalVisible = false;
      });
    } else {
      this.fileAlreadyExists = true;
    }
  }

  handleCancel(): void {
    this.newFileModalVisible = false;
    this.newSourceFile.fileName = '';
  }

  toggleSourceFile(file: ISourceFile): void {
    this.selectedSourceFile$.next(file);
    this.currentCompilationResult$.next(null);
  }

  private fetchSourceFiles(): void {
    this.sourceFilesService.getSourceFiles(this.projectId$.getValue()).subscribe(sourceFiles => {
      // Select the newly created sourcefile
      this.selectedSourceFile$.next(sourceFiles[sourceFiles.length - 1]);
      this.sourceFiles$.next(sourceFiles);
    });
  }

  isFileSelected(file: ISourceFile): Observable<boolean> {
    return this.selectedSourceFile$.pipe(map(currentFile => currentFile.fileName === file.fileName));
  }

}
