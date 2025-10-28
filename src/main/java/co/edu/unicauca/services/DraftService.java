package co.edu.unicauca.services;

import co.edu.unicauca.repositories.DraftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DraftService {
    @Autowired
    private DraftRepository _draftRepository;
}
