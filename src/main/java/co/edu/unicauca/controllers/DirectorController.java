package co.edu.unicauca.controllers;

import co.edu.unicauca.dtos.processes.ProcessRequestDTO;
import co.edu.unicauca.dtos.RejectFormatARequestDTO;
import co.edu.unicauca.services.degreework.DraftService;
import co.edu.unicauca.services.degreework.FormatAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/director")
public class DirectorController {
    @Autowired
    private FormatAService _formatAService;
    @Autowired
    private DraftService _draftService;

    @PostMapping("/submit/format-a")
    public ResponseEntity<?> submitFormatA(@RequestBody ProcessRequestDTO processRequest) {
        return ResponseEntity.ok(_formatAService.submitProcess(processRequest));
    }

    @PostMapping("/submit/draft")
    public ResponseEntity<?> submitDraft(@RequestBody ProcessRequestDTO processRequest) {
        return ResponseEntity.ok(_draftService.submitProcess(processRequest));
    }

    @PostMapping("/resubmit/format-a")
    public ResponseEntity<?> resubmitFormatA(@RequestBody RejectFormatARequestDTO request){
        return ResponseEntity.ok(_formatAService.resubmitProcess(request.getIdProcess(), request.getObservation()));
    }

}
