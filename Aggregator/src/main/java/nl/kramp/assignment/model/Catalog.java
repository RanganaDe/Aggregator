package nl.kramp.assignment.model;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Catalog {
	private String id;
	private String name;
	private String description;
	private Map<String, String> specs;
	private List<String> images;

}
