/*This DTO is used to send category data back to the client.
It returns only the ncessary category information in API response.
* */
package com.dreamcart.backend.dto.response;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CategoryResponse {

    private long id; // unique identifier of the category
    private String name;
    private String description;
}
