package com.localhelp.backend.controller;

import com.localhelp.backend.model.Medicine;
import com.localhelp.backend.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medicines")

public class MedicineController {

    @Autowired
    private MedicineRepository medicineRepo;

    // GET ALL MEDICINES
    @GetMapping
    public List<Medicine> getAllMedicines() {
        return medicineRepo.findAll();
    }
}
