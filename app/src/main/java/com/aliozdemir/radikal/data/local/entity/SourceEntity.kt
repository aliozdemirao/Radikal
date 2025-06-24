package com.aliozdemir.radikal.data.local.entity

import androidx.room.ColumnInfo

data class SourceEntity(

    @ColumnInfo(name = "source_id")
    val id: String?,

    @ColumnInfo(name = "source_name")
    val name: String?,
)