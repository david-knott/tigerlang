import {
    NgModule,
    ModuleWithProviders,
    Optional,
    SkipSelf
} from '@angular/core';
import { CommonModule } from '@angular/common';

// What Angular recommends is to put all of our global services in a separated module,
// called CoreModule, and import it ONLY in AppModule. This way is the same as providing
// the services in AppModule directly!
@NgModule({
    declarations: [],
    imports: [CommonModule]
})
export class CoreModule {
    constructor(@Optional() @SkipSelf() parentModule: CoreModule) {
        if (parentModule) {
            throw new Error(
                'CoreModule is already loaded. Import it in the AppModule only..'
            );
        }
    }

    static forRoot(): ModuleWithProviders {
        return {
            ngModule: CoreModule
        };
    }
}