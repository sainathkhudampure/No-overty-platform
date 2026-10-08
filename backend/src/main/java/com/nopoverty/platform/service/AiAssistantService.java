package com.nopoverty.platform.service;

import com.nopoverty.platform.dto.AiQuestionRequest;
import com.nopoverty.platform.model.Scheme;
import com.nopoverty.platform.model.SchemeDocument;
import com.nopoverty.platform.repository.SchemeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AiAssistantService {

    @Autowired
    private SchemeRepository schemeRepository;

    @Value("${AI_API_KEY:}")
    private String externalAiApiKey;

    public Map<String, Object> answerQuestion(AiQuestionRequest request) {
        String query = request.getQuestion() != null ? request.getQuestion().trim().toLowerCase() : "";
        Map<String, Object> response = new HashMap<>();
        response.put("disclaimer", "AI responses are informational. Always verify details on the official government portal.");

        List<Scheme> allSchemes = schemeRepository.findByIsActiveTrue();

        // 1. Direct scheme reference query
        if (request.getSchemeId() != null) {
            Optional<Scheme> schemeOpt = schemeRepository.findById(request.getSchemeId());
            if (schemeOpt.isPresent()) {
                Scheme scheme = schemeOpt.get();
                if (query.contains("document") || query.contains("doc")) {
                    StringBuilder docs = new StringBuilder("Required documents for **" + scheme.getName() + "**:\n");
                    for (SchemeDocument doc : scheme.getDocuments()) {
                        docs.append("• ").append(doc.getDocumentName())
                            .append(doc.getIsMandatory() ? " *(Mandatory)*" : " *(Optional)*")
                            .append(": ").append(doc.getDescription() != null ? doc.getDescription() : "")
                            .append("\n");
                    }
                    response.put("answer", docs.toString());
                    response.put("officialLink", scheme.getOfficialPortalUrl());
                    return response;
                } else if (query.contains("apply") || query.contains("process") || query.contains("how")) {
                    response.put("answer", "**Application Process for " + scheme.getName() + "**:\n" +
                            scheme.getApplicationProcess() + "\n\nVisit official portal: " + scheme.getOfficialApplicationUrl());
                    response.put("officialLink", scheme.getOfficialApplicationUrl());
                    return response;
                } else if (query.contains("benefit") || query.contains("amount") || query.contains("money")) {
                    response.put("answer", "**Benefits of " + scheme.getName() + "**:\n" + scheme.getBenefits());
                    response.put("officialLink", scheme.getOfficialPortalUrl());
                    return response;
                }
            }
        }

        // 2. Keyword/Category parsing
        if (query.contains("farmer") || query.contains("kisan") || query.contains("agriculture") || query.contains("crop")) {
            response.put("answer", "Here are prime welfare schemes available for **Farmers & Agriculture**:\n\n" +
                    "1. **PM Kisan Samman Nidhi (PM-KISAN)**: ₹6,000/year direct financial support in 3 installments for landholding farmers.\n" +
                    "2. **PMAY-G**: Up to ₹1.3 Lakh assistance for building pucca houses for rural households.\n\n" +
                    "Would you like to check your eligibility for PM-KISAN or view required land records?");
            response.put("relatedCategory", "farmer");
            return response;
        }

        if (query.contains("student") || query.contains("scholarship") || query.contains("education") || query.contains("college")) {
            response.put("answer", "Here are the top schemes for **Students & Education**:\n\n" +
                    "1. **National Post-Matric Scholarship (NSP)**: Tuition fee reimbursement and monthly stipend for Class 11, 12, ITI, Degree, and University students.\n" +
                    "2. **Sukanya Samriddhi Yojana (SSY)**: Government-backed sovereign 8.2% interest savings account for girl child education.\n\n" +
                    "Ensure you have your family Income Certificate (under ₹2.5 Lakhs) and previous marksheets ready!");
            response.put("relatedCategory", "student");
            return response;
        }

        if (query.contains("senior") || query.contains("pension") || query.contains("old age") || query.contains("elder")) {
            response.put("answer", "For **Senior Citizens (60+ years)**, key welfare programs include:\n\n" +
                    "1. **Indira Gandhi National Old Age Pension Scheme (IGNOAPS)**: Monthly non-contributory cash pension for elderly citizens from BPL households.\n" +
                    "2. **Ayushman Bharat (PM-JAY)**: Up to ₹5 Lakhs free inpatient health cover per family per year.\n\n" +
                    "Check your Aadhaar age proof and BPL card to verify eligibility!");
            response.put("relatedCategory", "senior_citizen");
            return response;
        }

        if (query.contains("vendor") || query.contains("loan") || query.contains("business") || query.contains("svanidhi")) {
            response.put("answer", "For **Street Vendors & Micro-Entrepreneurs**:\n\n" +
                    "1. **PM SVANidhi**: Collateral-free working capital micro-loans (₹10,000 -> ₹20,000 -> ₹50,000) with 7% interest subsidy and digital cashback.\n\n" +
                    "You need a Urban Local Body (ULB) vending certificate or Letter of Recommendation from your municipality.");
            response.put("relatedCategory", "employment");
            return response;
        }

        if (query.contains("disab") || query.contains("divyang") || query.contains("handicap")) {
            response.put("answer", "For **Persons with Benchmark Disabilities (Divyangjan)**:\n\n" +
                    "1. **ADIP Scheme**: 100% free modern assistive aids, motorized tricycles, braille kits, hearing aids, and prosthetics.\n\n" +
                    "You will need a Unique Disability ID (UDID) Card with 40%+ disability certificate.");
            response.put("relatedCategory", "disability");
            return response;
        }

        if (query.contains("women") || query.contains("girl") || query.contains("matern") || query.contains("child")) {
            response.put("answer", "For **Women and Children**:\n\n" +
                    "1. **Pradhan Mantri Matru Vandana Yojana (PMMVY)**: ₹5,000 direct maternity cash incentive for pregnant & lactating mothers.\n" +
                    "2. **Sukanya Samriddhi Yojana (SSY)**: High-yield sovereign deposit for girl child up to 10 years of age.\n\n" +
                    "Keep your Mother & Child Protection (MCP) card and bank passbook handy!");
            response.put("relatedCategory", "women_children");
            return response;
        }

        if (query.contains("health") || query.contains("hospital") || query.contains("ayushman") || query.contains("medical")) {
            response.put("answer", "For **Healthcare & Medical Relief**:\n\n" +
                    "1. **Ayushman Bharat PM Jan Arogya Yojana (AB PM-JAY)**: Free cashless hospitalization up to ₹5,00,000 per family per year across 27,000+ empaneled hospitals across India.\n\n" +
                    "Check your name using your Ration Card on beneficiary.nha.gov.in or visit the nearest Ayushman Mitra counter!");
            response.put("relatedCategory", "healthcare");
            return response;
        }

        // Default smart summary of available schemes
        response.put("answer", "I am your **AI Welfare Assistant** aligned with **UN SDG 1: No Poverty**.\n\n" +
                "I can assist you with:\n" +
                "• **Student Scholarships**: Post-matric scholarships, girl child education\n" +
                "• **Farmer Welfare**: PM-KISAN, crop insurance, rural housing\n" +
                "• **Senior Citizens**: Old age social pensions\n" +
                "• **Micro-Credit & Livelihood**: PM SVANidhi, MGNREGA\n" +
                "• **Healthcare**: Ayushman Bharat ₹5 Lakh health insurance\n" +
                "• **Document Checklists & Application Portals**\n\n" +
                "Try asking: *'What schemes are available for students?'* or *'How do I apply for PM-KISAN?'*");
        return response;
    }
}

