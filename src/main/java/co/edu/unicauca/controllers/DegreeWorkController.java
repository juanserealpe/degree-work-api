package co.edu.unicauca.controllers;

import co.edu.unicauca.dtos.degreework.DegreeWorkRequestDTO;
import co.edu.unicauca.entities.DegreeWork;
import co.edu.unicauca.services.degreework.DegreeWorkService;
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

    @PostMapping ("/create")
    public ResponseEntity<DegreeWork> createDegreeWork(@RequestBody DegreeWorkRequestDTO requestDTO){
        DegreeWork degreeWork = _degreeWorkService.createDegreeWork(requestDTO);
        return ResponseEntity.ok(degreeWork);
    }
}
