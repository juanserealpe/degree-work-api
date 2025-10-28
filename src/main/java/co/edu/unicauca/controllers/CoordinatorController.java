package co.edu.unicauca.controllers;


import co.edu.unicauca.dtos.ProcessRequestDTO;
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

    @PostMapping("/approve-format-a")
    public ResponseEntity<?> approveFormatA(@RequestBody Long idProcess) {
        return ResponseEntity.ok(_formatAService.approveProcess(idProcess));
    }
}
