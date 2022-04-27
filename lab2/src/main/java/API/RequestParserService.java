package API;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class RequestParserService {

    public ArrayList<Long> longArrayParse(Map<String, String[]> params, String param)  {

        return (ArrayList<Long>) Arrays.stream(params.get(param))
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }

    public HashMap<Long, Short> getMarks(Map<String, String[]> parameters) {
        return (HashMap<Long, Short>) parameters.keySet()
                .stream()
                .filter(param -> param.matches("[0-9]+"))
                .collect(Collectors.toMap(
                        Long::parseLong,
                        param -> Arrays.stream(parameters.get(param))
                                .map(Short::parseShort)
                                .findFirst().get()
                        )
                );
    }
}
