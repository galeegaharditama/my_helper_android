package com.galih.library_helper_class.widget

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/*
 * https://github.com/JoaquimLey/avenging/blob/development/mobile/src/main/java/com/joaquimley/avenging/ui/list/EndlessRecyclerViewOnScrollListener.java
 * Copyright (c) Joaquim Ley 2016. All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
abstract class EndlessRecyclerViewOnScrollListener(
    private val layoutManager: RecyclerView.LayoutManager,
    private val withLoadingView: Boolean = false
) : RecyclerView.OnScrollListener() {

  private var currentPage = 0
  private var previousTotalItemCount = 0
  private var loading = true
  var isNetworkRequesting = false

  /**
   * Low threshold to show the onLoad()/Spinner functionality.
   * If you are going to use this for a production app set a higher value
   * for better UX
   */
  private var sVisibleThreshold = 2

  init {
    if (layoutManager is GridLayoutManager) {
      sVisibleThreshold *= layoutManager.spanCount
    } else if (layoutManager is StaggeredGridLayoutManager) {
      sVisibleThreshold *= layoutManager.spanCount
    }
  }

  private fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
    var maxSize = 0
    for (i in lastVisibleItemPositions.indices) {
      if (i == 0 || lastVisibleItemPositions[i] > maxSize) {
        maxSize = lastVisibleItemPositions[i]
      }
    }
    return maxSize
  }

  override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
    var lastVisibleItemPosition = 0
    val totalItemCount = layoutManager.itemCount

    when (layoutManager) {
      is StaggeredGridLayoutManager -> {
        val lastVisibleItemPositions = layoutManager.findLastVisibleItemPositions(null)
        lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions)
      }
      is GridLayoutManager -> {
        lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
      }
      is LinearLayoutManager -> {
        lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
      }
    }

    // List was cleared
    if (totalItemCount < previousTotalItemCount) {
      currentPage = STARTING_PAGE_INDEX
      previousTotalItemCount = 0
      isNetworkRequesting = false

      if (totalItemCount == 0) {
        loading = true
      }
    }

    /**
     * If it’s still loading, we check to see if the DataSet count has
     * changed, if so we conclude it has finished loading and update the current page
     * number and total item count.
     */
    val previousItemCount = if (withLoadingView) previousTotalItemCount + 1
    else previousTotalItemCount

    if (loading && totalItemCount > previousItemCount) {
      loading = false
      previousTotalItemCount = totalItemCount
    }

    /**
     * If it isn’t currently loading, we check to see if we have breached
     * + the visibleThreshold and need to reload more data.
     */
    if (!loading && lastVisibleItemPosition + sVisibleThreshold > totalItemCount &&
      !isNetworkRequesting) {
      currentPage++
      onLoadMore(currentPage, totalItemCount)
      loading = true
    }
  }

  fun reset() {
    currentPage = 0
    previousTotalItemCount = 0
    loading = true
  }

  abstract fun onLoadMore(page: Int, totalItemsCount: Int)

  companion object {
    private const val STARTING_PAGE_INDEX = 0
  }
}
