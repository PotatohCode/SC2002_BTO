package BTO;
import java.util.List;
import java.util.Date;
import java.util.stream.Collectors;



public class FilterUtil {
	public static List<BTO> applyUserFilters(List<BTO> btoList, Users user) {
	    return btoList.stream()
	        .filter(b -> user.getFilterNeighbourhood().isEmpty() || b.getNeighbourhood().toLowerCase().contains(user.getFilterNeighbourhood().toLowerCase()))
	        .filter(b -> user.getFilterRoomType() == -1 ||
	                    (user.getFilterRoomType() == 2 && b.getNum2Rooms() > 0) ||
	                    (user.getFilterRoomType() == 3 && b.getNum3Rooms() > 0))
	        .filter(b -> !user.isFilterOpenOnly() || (new Date().after(b.getApplicationStart()) && new Date().before(b.getApplicationEnd())))
	        .filter(b -> user.getFilterProjectName().isEmpty() || b.getName().toLowerCase().contains(user.getFilterProjectName().toLowerCase()))
	        .toList();
	}

}
