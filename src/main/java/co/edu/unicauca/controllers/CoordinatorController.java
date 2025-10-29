package co.edu.unicauca.controllers;

import co.edu.unicauca.dtos.AssignJuryRequestDTO;
import co.edu.unicauca.dtos.RejectFormatARequestDTO;
import co.edu.unicauca.services.DraftService;
import co.edu.unicauca.services.FormatAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coordinator")
public class CoordinatorController {
    @Autowired
    private FormatAService _formatAService;
    @Autowired
    private DraftService _draftService;

    @PostMapping("/approve-format-a")
    public ResponseEntity<?> approveFormatA(@RequestBody Long idProcess) {
        return ResponseEntity.ok(_formatAService.approveProcess(idProcess));
    }

    @PostMapping("/reject-format-a")
    public ResponseEntity<?> rejectFormatA(@RequestBody RejectFormatARequestDTO request) {
        return ResponseEntity.ok(_formatAService.rejectProcess(request.getIdProcess(), request.getObservation()));
    }

    @PostMapping("/reject-draft")
    public ResponseEntity<?> rejectDraft(@RequestBody RejectFormatARequestDTO request) {
        return ResponseEntity.ok(_draftService.rejectProcess(request.getIdProcess(), request.getObservation()));
    }

    @PostMapping("/approve-draft")
    public ResponseEntity<?> approveDraft(@RequestBody Long idProcess) {
        return ResponseEntity.ok(_draftService.approveProcess(idProcess));
    }

    @PostMapping("/draft/assign-evaluators")
    public ResponseEntity<?> assignEvaluators(@RequestBody AssignJuryRequestDTO request){
        return ResponseEntity.ok(_draftService.assignJuryToDraft(request));
    }
}
