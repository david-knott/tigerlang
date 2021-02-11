package hello;

import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chaosopher.tigerlang.compiler.sugar.Desugar;
import com.chaosopher.tigerlang.compiler.absyn.Absyn;
import com.chaosopher.tigerlang.compiler.absyn.PrettyPrinter;
import com.chaosopher.tigerlang.compiler.bind.Binder;
import com.chaosopher.tigerlang.compiler.errormsg.ErrorMsg;
import com.chaosopher.tigerlang.compiler.parse.CupParser;
import com.chaosopher.tigerlang.compiler.parse.Parser;
import com.chaosopher.tigerlang.compiler.parse.ParserFactory;
import com.chaosopher.tigerlang.compiler.parse.ParserService;
import com.chaosopher.tigerlang.compiler.parse.Program;

import com.chaosopher.tigerlang.compiler.canon.CanonicalizationImpl;
import com.chaosopher.tigerlang.compiler.errormsg.ErrorMsg;
import com.chaosopher.tigerlang.compiler.parse.ParserFactory;
import com.chaosopher.tigerlang.compiler.parse.ParserService;
import com.chaosopher.tigerlang.compiler.regalloc.RegAllocFactory;
import com.chaosopher.tigerlang.compiler.util.TaskRegister;
import com.chaosopher.tigerlang.compiler.util.Timer;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@RestController
@CrossOrigin(origins = "*")
public class Application {

	private ParserService parserService;

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
			//	registry.addMapping("/compile").allowedOrigins("http://localhost:4200");
			//	registry.addMapping("/**");
			}
		};
	}

	public Application() {
		parserService = new ParserService(new ParserFactory());
	}

	@PostMapping("/compile")
	public CompilerResponse compile(@RequestBody TigerSource tigerSource) {
		InputStream in = new ByteArrayInputStream(tigerSource.getCode().getBytes());
		ByteArrayOutputStream backingOutputStream = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(backingOutputStream);
		ByteArrayOutputStream backingErrorStream = new ByteArrayOutputStream();
		PrintStream err = new PrintStream(backingErrorStream);
		ErrorMsg errorMsg = new ErrorMsg("", err);
		ArrayList<String> argList = new ArrayList<>();
		if(tigerSource.isEscapes()) {
			argList.add("--escapes-compute");
		}
		if(tigerSource.isEscapesDisplay()) {
			argList.add("--escapes-display");
		}
		if(tigerSource.isBindingsDisplay()) {
			argList.add("--bindings-display");
		}
		if(tigerSource.isStaticLinks()) {
			argList.add("--optimise-staticlinks");
		}
		if(tigerSource.isStaticLinkEscapes()) {
			argList.add("--optimise-staticlinks-escapes");
		}
		if(tigerSource.isInline()) {
			argList.add("--inline");
		}
		if(tigerSource.isPrune()) {
			argList.add("--prune");
		}
		if(tigerSource.isRename()) {
			argList.add("--rename");
		}
		if(tigerSource.isDesugarForLoop()) {
			argList.add("--desugar");
		}
		if(tigerSource.isDesugarStringComp()) {
			argList.add("--desugar");
		}
		if(tigerSource.isNoPrelude()) {
			argList.add("--no-prelude");
		}
		if(tigerSource.isAstDisplay()) {
			argList.add("--ast-display");
		}
		if(tigerSource.isHirDisplay()) {
			argList.add("--hir-display");
		}
		if(tigerSource.isLirDisplay()) {
			argList.add("--lir-display");
		}
		if(tigerSource.isRegAlloc()) {
			argList.add("--reg-alloc");
		}
		if(tigerSource.isDemove()) {
			argList.add("--demove");
		}
		if(tigerSource.isCfg()) {
			argList.add("--cfg");
		}
		if(tigerSource.isCallGraphDisplay()) {
			argList.add("--callgraph-display");
		}
		argList.add("filenameplaceholder");
		//String[] args = new String[] {"--reg-alloc", "--escapes-compute", "--demove", "filenameplaceholder"};
		String[] args = argList.toArray(new String[argList.size()]);
		new TaskRegister()
		.register(new com.chaosopher.tigerlang.compiler.main.Tasks())
		.register(new com.chaosopher.tigerlang.compiler.parse.Tasks(new ParserService(new ParserFactory())))
		.register(new com.chaosopher.tigerlang.compiler.callgraph.Tasks())
		.register(new com.chaosopher.tigerlang.compiler.absyn.Tasks())
		.register(new com.chaosopher.tigerlang.compiler.sugar.Tasks())
		.register(new com.chaosopher.tigerlang.compiler.findescape.Tasks())
		.register(new com.chaosopher.tigerlang.compiler.bind.Tasks())
		.register(new com.chaosopher.tigerlang.compiler.staticlink.Tasks())
		.register(new com.chaosopher.tigerlang.compiler.inlining.Tasks())
		.register(new com.chaosopher.tigerlang.compiler.types.Tasks())
		.register(new com.chaosopher.tigerlang.compiler.translate.Tasks())
		.register(new com.chaosopher.tigerlang.compiler.dataflow.Tasks())
		.register(new com.chaosopher.tigerlang.compiler.canon.Tasks(new CanonicalizationImpl()))
		.register(new com.chaosopher.tigerlang.compiler.target.Tasks())
		.register(new com.chaosopher.tigerlang.compiler.intel.Tasks(null, null))
		.register(new com.chaosopher.tigerlang.compiler.regalloc.Tasks(new RegAllocFactory()))
		.parseArgs(args)
		.execute(in, out, err, errorMsg);
		return new CompilerResponse(backingOutputStream.toString(), backingErrorStream.toString(), String.join(" ", args));
	}

	@GetMapping("/")
	public String index() {
		return "index";
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
