/*
 * Copyright (C) 2017 Karumi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.karumi.screenshot.usecase;

import android.os.Handler;
import android.os.Looper;

import com.karumi.screenshot.model.Result;
import com.karumi.screenshot.model.SuperHero;
import com.karumi.screenshot.model.SuperHeroDetailError;
import com.karumi.screenshot.model.SuperHeroListError;
import com.karumi.screenshot.model.SuperHeroesRepository;
import java.util.List;
import javax.inject.Inject;

public class GetSuperHeroes {

  private final SuperHeroesRepository repository;

  @Inject public GetSuperHeroes(SuperHeroesRepository repository) {
    this.repository = repository;
  }

  public void getAll(final Callback callback) {
    new Thread(new Runnable() {
      @Override public void run() {
        loadSuperHeroes(callback);
      }
    }).start();
  }

  private void loadSuperHeroes(final Callback callback) {
    final Result<List<SuperHero>,SuperHeroListError> superHeroesResult = repository.getAll();

    if(!superHeroesResult.hasError()){
      new Handler(Looper.getMainLooper()).post(new Runnable() {
        @Override public void run() {
          callback.onSuperHeroesLoaded(superHeroesResult.getValue());
        }
      });

    }else {
      new Handler(Looper.getMainLooper()).post(new Runnable() {
        @Override public void run() {
          callback.onError(superHeroesResult.getError());
        }
      });
    }
  }

  public interface Callback {

    void onSuperHeroesLoaded(List<SuperHero> superHeroes);
    void onError(SuperHeroListError error);
  }
}
