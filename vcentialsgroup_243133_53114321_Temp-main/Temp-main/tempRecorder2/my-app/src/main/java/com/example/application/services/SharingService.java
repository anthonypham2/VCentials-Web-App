package com.example.application.services;

import com.example.application.data.RecordDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Service class responsible for handling sharing operations.
 */
@Service
public class SharingService {

    private static final Logger logger = LoggerFactory.getLogger(SharingService.class);

    @Autowired
    private RecordServices recordServices;  // Injecting the existing RecordServices to use its methods

    /**
     * Formats the data for sharing by retrieving records from the RecordServices.
     * @return A formatted string containing the record information.
     */
    public String formatDataForSharing() {
        logger.info("Formatting data for sharing");
        // Using RecordServices to get records
        List<RecordDTO> records = recordServices.getRecords();
        StringBuilder formattedData = new StringBuilder();
        for (RecordDTO record : records) {
            formattedData.append(formatRecord(record)).append("\n");
        }
        return formattedData.toString();
    }

    /**
     * Formats a single RecordDTO object into a readable string using reflection.
     * @param record The RecordDTO object to format.
     * @return A formatted string representation of the record.
     */
    private String formatRecord(RecordDTO record) {
        StringBuilder sb = new StringBuilder();
        sb.append("RecordDTO{");
        try {
            Field[] fields = RecordDTO.class.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                sb.append(field.getName()).append("='").append(field.get(record)).append("', ");
            }
        } catch (IllegalAccessException e) {
            logger.error("Error accessing field", e);
        }
        sb.setLength(sb.length() - 2); // Remove the trailing comma and space
        sb.append('}');
        return sb.toString();
    }

    /**
     * Shares the formatted data through a specified channel. This method can be extended to include actual sharing logic.
     * @param channel The channel through which the data will be shared.
     */
    public void shareData(String channel) {
        String formattedData = formatDataForSharing();
        // Implement actual sharing logic here
        logger.info("Sharing data on " + channel + ": " + formattedData);
    }
}
