package com.example.basicweatherapplication.converters

import com.example.basicweatherapplication.model.location.LocationModel
import com.example.basicweatherapplication.network.location.jsonData.LocationItem

fun LocationItem.convertToLocationDetailsModel()  = LocationModel(this.name)