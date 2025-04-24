package BTO;
import java.util.List;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * Utility class for applying search filters to BTO project listings
 * based on user-specific criteria (such as neighbourhood, room type, 
 * application status, and project name).
 * 
 * This class is stateless and only provides static helper methods.
 * It is used by different user roles (e.g., Applicant, Officer)
 * to filter project listings based on their personalised settings.
 * 
 * @author Kah Teck, Keanan, Javier, Junnoske, Kevin
 * @version 23/04/2025
 */

public class FilterUtil {
    /**
     * Applies user-defined search filters to a list of BTO projects.
     * The filters are based on the current user's preferences, which include:
     * <ul>
     *     <li>Neighbourhood keyword (case-insensitive)</li>
     *     <li>Room type availability (2-room or 3-room)</li>
     *     <li>Project open status (start and end date)</li>
     *     <li>Project name keyword (case-insensitive)</li>
     * </ul>
     * 
     * @param btoList the list of all BTO projects to filter
     * @param user the user whose filter settings will be used
     * @return a filtered list of BTO projects matching the user's criteria
     */
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
