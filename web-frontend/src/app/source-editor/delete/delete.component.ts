import { Component, Input, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { ModalService } from "@app/services/modal.service";
import { Source } from "@app/services/source";
import { SourceService } from "@app/services/source.service";

@Component({
  selector: "app-delete",
  templateUrl: "./delete.component.html",
  styleUrls: ["./delete.component.scss"],
})
export class DeleteComponent implements OnInit {
  constructor(
    private modalService: ModalService,
    private sourceService: SourceService,
    private router: Router
  ) {}

  @Input()
  source: Source = null;

  public close() {
    this.modalService.destroy();
  }

  public confirm() {
    console.log(this.source);
    this.sourceService.deleteSource(this.source).subscribe((s) => {
      this.modalService.destroy();
      this.router.navigateByUrl("/source");
    });
  }

  ngOnInit() {}
}
