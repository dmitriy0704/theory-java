package dev.folomkin.testing.mockito;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;
import java.util.List;
import java.util.function.Supplier;

public interface DataService {

    void saveData(List<String> dataToSave);

    String getDataById(String id);

    String getDataById(String id, Supplier<String> calculateIfAbsent);

    List<String> getData();

    List<String> getDataListByIds(List<String> idList);

    List<String> getDataByRequest(DataSearchRequest request);

}


@AllArgsConstructor
@Getter
class DataSearchRequest {

    String id;

    Date updatedBefore;

    int length;
}