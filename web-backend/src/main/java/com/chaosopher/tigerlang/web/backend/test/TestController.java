package com.chaosopher.tigerlang.web.backend.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import com.chaosopher.tigerlang.compiler.canon.CanonicalizationImpl;
import com.chaosopher.tigerlang.compiler.errormsg.ErrorMsg;
import com.chaosopher.tigerlang.compiler.parse.ParserFactory;
import com.chaosopher.tigerlang.compiler.parse.ParserService;
import com.chaosopher.tigerlang.compiler.regalloc.RegAllocFactory;
import com.chaosopher.tigerlang.compiler.util.TaskRegister;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class TestController {

	@PostMapping("/compile")
	public CompilerResponse compile(@RequestBody TigerSource tigerSource) {
		InputStream in = new ByteArrayInputStream(tigerSource.getCode().getBytes());
		ByteArrayOutputStream backingOutputStream = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(backingOutputStream);
		ByteArrayOutputStream backingErrorStream = new ByteArrayOutputStream();
		PrintStream err = new PrintStream(backingErrorStream);
		ErrorMsg errorMsg = new ErrorMsg("", err);
		ArrayList<String> argList = new ArrayList<>();
		if(tigerSource.isEscapesCompute()) {
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
		if(tigerSource.isCfg()) {
			argList.add("--cfg");
		}
		if(tigerSource.isCallGraphDisplay()) {
			argList.add("--callgraph-display");
		}
		if(tigerSource.isHirDisplay()) {
			argList.add("--hir-display");
		}
//		if(tigerSource.isDeatomize()) {
	//		argList.add("--optimize");
			//argList.add("--deatomize");
//		} else {
		//	argList.add("--optimize");
//			argList.add("--lir-compute");
		//}
		if(tigerSource.isLirDisplay()) {
			argList.add("--optimize");
			argList.add("--lir-compute");
			argList.add("--lir-display");
		}
		if(tigerSource.isRegAlloc()) {
			argList.add("--optimize");
			argList.add("--lir-compute");
			argList.add("--reg-alloc");
		}
		if(tigerSource.isDemove()) {
			argList.add("--demove");
		}
		argList.add("filenameplaceholder");
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
}
