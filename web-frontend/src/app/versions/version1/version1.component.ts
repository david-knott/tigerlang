import {
  Component,
  ElementRef,
  OnInit,
  ViewChild,
  ViewEncapsulation,
} from "@angular/core";
import { Source } from "@app/services/source";
import { SourceService } from "../../services/source.service";
import { ErrorCheckService } from "../../services/error-check.service";
import { map, mergeMap, catchError } from "rxjs/operators";
import { ErrorCheckResponse } from "@app/services/error";
import { Router, ActivatedRoute, ParamMap } from "@angular/router";
import { PrettyPrinterService } from "@app/services/pretty-printer.service";
import { ModalService } from "@app/services/modal.service";
//import { DeleteComponent } from "./delete/delete.component";



@Component({
  selector: 'app-version1',
  templateUrl: './version1.component.html',
  styleUrls: ['./version1.component.scss']
})
export class Version1Component implements OnInit {

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private sourceService: SourceService,
    private errorCheckService: ErrorCheckService,
    private prettyPrintService: PrettyPrinterService,
    private window: Window,
    private document: Document,
    private modalService: ModalService
  ) {}

  source: Source;
  srcHighlightedLines: Array<number>;
  dstHighlightedLines: Array<number>;

  setSource(source: Source) {
    this.source = source;
  }

  test() {
    //this.srcHighlightedLines = [1];
  }

  ngOnInit() {
    this.route.params.subscribe((params) => {
      if (params["id"]) {
        this.sourceService
          .getSource(params["id"])
          .pipe(
            map((source) => this.setSource(source)),
            mergeMap(() =>
              this.errorCheckService.check({ tiger: this.source.code })
            )
          )
          .subscribe((error) => (error));
      } else {
        this.setSource({name: "Untitled", description: "", code: "/* new program */"});
      }
    });
  }
}
