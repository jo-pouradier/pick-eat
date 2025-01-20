package fr.pick_eat.restaurant.mapper;

import fr.pick_eat.restaurant.dto.RestaurantAvisDTO;
import fr.pick_eat.restaurant.dto.RestaurantDTO;
import fr.pick_eat.restaurant.dto.RestaurantNoteDTO;
import fr.pick_eat.restaurant.entity.RestaurantAvisModel;
import fr.pick_eat.restaurant.entity.RestaurantModel;
import fr.pick_eat.restaurant.entity.RestaurantNoteModel;

public class RestaurantMapper {
    public static RestaurantDTO toRestaurantDTO(RestaurantModel restaurant) {
        RestaurantDTO restaurantDTO = new RestaurantDTO();
        restaurantDTO.setId(restaurant.getId());
        restaurantDTO.setName(restaurant.getName());
        restaurantDTO.setAddress(restaurant.getAddress());
        restaurantDTO.setLat(restaurant.getLat());
        restaurantDTO.setLon(restaurant.getLon());
        restaurantDTO.setTypes(restaurant.getTypes());
        restaurantDTO.setBucket(restaurant.getBucket());
        restaurantDTO.setPicture(restaurant.getPicture());
        restaurantDTO.setGeneratedPicture(restaurant.getGeneratedPicture());
        restaurantDTO.setPrice_level(restaurant.getPrice_level());
        restaurantDTO.setDescription(restaurant.getDescription());
        restaurantDTO.setIcons(restaurant.getIcons());
        return restaurantDTO;
    }


    public static RestaurantModel toRestaurantModel(RestaurantDTO restaurantDTO) {
        RestaurantModel restaurant = new RestaurantModel();
        restaurant.setId(restaurantDTO.getId());
        restaurant.setName(restaurantDTO.getName());
        restaurant.setAddress(restaurantDTO.getAddress());
        restaurant.setLat(restaurantDTO.getLat());
        restaurant.setLon(restaurantDTO.getLon());
        restaurant.setTypes(restaurantDTO.getTypes());
        restaurant.setBucket(restaurantDTO.getBucket());
        restaurant.setPicture(restaurantDTO.getPicture());
        restaurant.setGeneratedPicture(restaurantDTO.getGeneratedPicture());
        restaurant.setPrice_level(restaurantDTO.getPrice_level());
        restaurant.setDescription(restaurantDTO.getDescription());
        return restaurant;
    }

    public static RestaurantAvisDTO toRestaurantAvisDTO(RestaurantAvisModel entity) {
        RestaurantAvisDTO dto = new RestaurantAvisDTO();
        dto.setId(entity.getId());
        dto.setRestaurantId(entity.getRestaurantId());
        dto.setComment(entity.getComment());
        dto.setUserId(entity.getUserId());
        return dto;
    }

    public static RestaurantNoteDTO toRestaurantNoteDTO(RestaurantNoteModel entity) {
        RestaurantNoteDTO dto = new RestaurantNoteDTO();
        dto.setId(entity.getId());
        dto.setRestaurantId(entity.getRestaurantId());
        dto.setNote(entity.getNote());
        dto.setUserId(entity.getUserId());
        return dto;
    }
}
