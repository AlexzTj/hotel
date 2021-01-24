package com.hotel.demo.repository;

import com.hotel.demo.model.Room;
import java.util.List;

public interface RoomRepository {

    List<Room> findAll();
}
