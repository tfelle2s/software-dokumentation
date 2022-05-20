package org.arc42.analyse.control;


import org.arc42.analyse.control.service.Arc42AnalyseService;

public class ResultFormatterService {

    private final Integer arcId;
    private final Arc42AnalyseService service;
    private String output;

    public ResultFormatterService(Integer arcId) {
        this.arcId = arcId;
        service = Arc42AnalyseService.getInstance();
    }

    private void formatString(String output) {
        this.output = output;
    }

    public String saveAnalyseResult(String output) {
        formatString(output);
        service.deleteAnalyseResult(this.arcId);
        service.saveAnalseResult(this.arcId, output);
        return this.output;
    }

// --Commented out by Inspection START (01.05.22, 20:57):
//    public String getOutput() {
//        return output;
//    }
// --Commented out by Inspection STOP (01.05.22, 20:57)

// --Commented out by Inspection START (01.05.22, 20:57):
//    public void setOutput(String output) {
//        this.output = output;
//    }
// --Commented out by Inspection STOP (01.05.22, 20:57)
}
