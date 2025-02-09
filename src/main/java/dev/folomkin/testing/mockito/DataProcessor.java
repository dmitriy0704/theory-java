package dev.folomkin.testing.mockito;

public class DataProcessor {

    private final DataService dataService;

    public DataProcessor(DataService dataService) {
        this.dataService = dataService;
    }

    public double processData() {
        return dataService.retrieveData() * 2;
    }
}
