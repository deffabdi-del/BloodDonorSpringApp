package com.deff.blooddonor;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.naming.Binding;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class BloodDonorController {

    @GetMapping
    public String showDonorForm (Model model) {
        model.addAttribute("donor", new Donor());
        return "donor_form";

    }

    @PostMapping("/submitDonorForm")
    public String processDonorForm(@Valid Donor donor, BindingResult bindingResult, Model model) {

        if (bindingResult.hasFieldErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            Map<String, String> validationErrorMap = ErrorUtils.createErrorMap(fieldErrors);
            model.addAttribute("validationErrorMap", validationErrorMap);
            return "donor_form";
        }
        else {// no errors. is the donor eligible or not eligible ?
            String resultMessage;
            List<String>ineligibleReasons = new ArrayList<>();

            if (donor.getAge() < 16) {
                ineligibleReasons.add("Age must be at least 16");
            }

            if (donor.getWeight() < 110) {
                ineligibleReasons.add("Weight must be at least 110lbs");
            }

            // look at length of list. If there is data in the list, user is not eligible
           // if the list is empty, the user is eligible.

            if (ineligibleReasons.isEmpty()) {
                resultMessage = "You may eligible to donate blood";
            } else {
                resultMessage = "You are NOT eligible to donate blood";
                model.addAttribute("ineligibleReasons", ineligibleReasons); // only add if donor is not eligible
            }
            model.addAttribute("resultMessage", resultMessage);
            return "results";
        }


    }
}
