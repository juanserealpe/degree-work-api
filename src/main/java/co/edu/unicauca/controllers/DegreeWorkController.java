package co.edu.unicauca.controllers;

import co.edu.unicauca.dtos.DegreeWorkRequestDTO;
import co.edu.unicauca.dtos.FormatARequestDTO;
import co.edu.unicauca.entities.DegreeWork;
import co.edu.unicauca.entities.FormatA;
import co.edu.unicauca.services.DegreeWorkService;
import co.edu.unicauca.services.FormatAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/degreework")
public class DegreeWorkController {

    @Autowired
    private DegreeWorkService _degreeWorkService;
    @Autowired
    private FormatAService _formatAService;

    @PostMapping ("/create")
    public ResponseEntity<DegreeWork> createDegreeWork(@RequestBody DegreeWorkRequestDTO requestDTO){
        DegreeWork degreeWork = _degreeWorkService.createDegreeWork(requestDTO);
        return ResponseEntity.ok(degreeWork);
    }
    @PostMapping("formatA/create")
    public ResponseEntity<FormatA> createFormatA(@RequestBody FormatARequestDTO requestDTO){
        FormatA formatA = _formatAService.createFormatA(requestDTO);
        return ResponseEntity.ok(formatA);
    }
}
