package com.chaosopher.tigerlang.web.backend.dataflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.chaosopher.tigerlang.compiler.dataflow.cfg.BasicBlock;
import com.chaosopher.tigerlang.compiler.dataflow.cfg.CFG;
import com.chaosopher.tigerlang.compiler.graph.Node;
import com.chaosopher.tigerlang.compiler.tree.QuadruplePrettyPrinter;
import com.chaosopher.tigerlang.compiler.tree.Stm;
import com.chaosopher.tigerlang.web.backend.CompilerRequest;
import com.chaosopher.tigerlang.web.backend.services.CompilerService;
import com.chaosopher.tigerlang.web.backend.services.ReDefDataFlowService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


class ReDefResponse {

    private Map<Integer, List<Integer>> succs = new HashMap<>();
    private Map<Integer, List<String>> nodeLabels = new HashMap<>();

    public void addNode(int hashCode) {
        this.succs.put(hashCode, new ArrayList<>());
        this.nodeLabels.put(hashCode, new ArrayList<>());
    }

    public void addEdge(int hashCode, int succ) {
        this.succs.get(hashCode).add(succ);
    }

    public void addNodeLabel(int hashCode, String string) {
        this.nodeLabels.get(hashCode).add(string);
    }

    public Map<Integer,List<Integer>> getNodes() {
        return this.succs;
    }

    public Map<Integer,List<String>> getLabels() {
        return this.nodeLabels;
    }

    public void addEdgeLabel(int hashCode, int hashCode2, Set<Integer> bout) {
    }
}

/**
 * Test Request: curl -X POST  localhost:8080/reachingDefs -H 'Content-type:application/json' -d '{"escapesCompute" : true, "code" : "var a:int := 3\nvar b:int := 5"}' 
 * @param translateRequest
 * @return a control flow graph of the basic blocks of the program.
 */
@RestController
@CrossOrigin(origins = "*")
public class ReachingDefinitionsController {

    @Autowired
    CompilerService compilerService;
    @Autowired
    ReDefDataFlowService dataFlowService;

    @PostMapping("/reachingDefs")
    public ResponseEntity<ReDefResponse> reachingDefs(@RequestBody CompilerRequest compilerRequest) {
        this.compilerService.parse(compilerRequest);
        if(this.compilerService.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        this.compilerService.bindAndTypeCheck();
        if(this.compilerService.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        this.compilerService.hirTranslate();
        // prepare hir for data flow analysis.ss
        this.dataFlowService.init(this.compilerService.getFragList());
        // get the control flow graph.
        CFG cfg = this.dataFlowService.getCFG();
        ReDefResponse rdResponse = new ReDefResponse();
        // loop through all nodes in cfg.
        for(Node node : cfg.nodes()) {
            rdResponse.addNode(node.hashCode());
            for(Node sucessor : node.succ()) {
                rdResponse.addEdge(node.hashCode(), sucessor.hashCode());
            }
        }
        // Get node data and edge data
        for(Node node : cfg.nodes()) {
            // get all the statements in a block.
            BasicBlock basicBlock = this.dataFlowService.getBasicBlock(node);
            // get gen and kill for the block and the statements.
            for(Stm stm : basicBlock.first) {
                Set<Integer> sgen = this.dataFlowService.getGen(stm);
                Set<Integer> skill = this.dataFlowService.getKill(stm);
                rdResponse.addNodeLabel(node.hashCode(), QuadruplePrettyPrinter.apply(stm) + " gen:" + sgen + " kill:" + skill);

             //   Set<Integer> sin = this.dataFlowService.getIn(basicBlock, stm);
             //   Set<Integer> sout = this.dataFlowService.getOut(basicBlock, stm);
            }
            Set<Integer> bgen = this.dataFlowService.getGen(basicBlock);
            Set<Integer> bkill = this.dataFlowService.getKill(basicBlock);
            rdResponse.addNodeLabel(node.hashCode(), "gen:"  + bgen + " kill:" + bkill);
            //Set<Integer> bin = this.dataFlowService.getIn(basicBlock);

            Set<Integer> bout = this.dataFlowService.getOut(basicBlock);
            rdResponse.addNodeLabel(node.hashCode(), "out:"  + bout);
        }
        return ResponseEntity.ok(rdResponse);
    }
}