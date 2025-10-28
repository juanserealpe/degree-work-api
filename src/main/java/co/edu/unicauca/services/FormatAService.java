package co.edu.unicauca.services;

import co.edu.unicauca.dtos.FormatARequestDTO;
import co.edu.unicauca.entities.FormatA;
import co.edu.unicauca.repositories.FormatARepository;
import co.edu.unicauca.validators.FormatAValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FormatAService {
    @Autowired
    private FormatAValidator _validator;
    @Autowired
    private FormatARepository _formatARepository;

    @Transactional
    public FormatA createFormatA(FormatARequestDTO dto) {
        return null;
    }
}
