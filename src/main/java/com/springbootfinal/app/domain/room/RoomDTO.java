package com.springbootfinal.app.domain.room;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomDTO {

    private int roomNo;
    private String roomName;
    private int pricePerNight;
    private String residName;

}
