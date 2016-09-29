package com.nikhu.ecommerce.shoppingcart

import com.nikhu.ecommerce.shoppingcart.Cart.State
import com.nikhu.ecommerce.shoppingcart.CartStatus.CartStatus
import ddd.cqrs.{AggregateRoot, AggregateState}

/**
  * Created by vsubbarravuri on 8/29/16.
  */

object Cart {

  case class State(
                    cartId: CartId,
                    status: CartStatus = CartStatus.Created,
                    items: List[CartItem] = List.empty[CartItem]
                  ) extends AggregateState[State] {

    override def apply = {
      case CartCreated(_, cartId, items) =>
        copy(items = items ::: items)

      case AddedItem(_, cartId, item) =>
        copy(items = items :+ item)

      case RemovedItem(_, cartId, item) =>
        copy(items = items.filter(_ != item))

      case AbandonedCart(_, _) =>
        copy(status = CartStatus.Abandoned)

      case ExpiredCart(_, _) =>
        copy(status = CartStatus.Expired)

      case DeletedCart(_, _) =>
        copy(status = CartStatus.Deleted)

      case CheckoutCart(_, _) =>
        copy(status = CartStatus.Checkout)
    }

  }

}

class Cart extends AggregateRoot[State] {

  // self.path.name is the entity identifier (utf-8 URL-encoded)
  override def persistenceId: String = "Cart-" + self.path.name

  override val factory: AggregateRootFactory = {
    case CartCreated(_, cartId, items) => {
      log.debug("Creating Cart.State")
      State(cartId, CartStatus.Created, items)
    }
  }


  override def handleCommand: Receive = {
    case cmd: CreateCart => {
      log.debug("Received command to create cart with cartId {}", cmd.cartId)
      raise(CartCreated(cmd.id, cmd.cartId, cmd.items))
    }
    case cmd: AddToCart => {
      log.debug("Received command to add item {} with cartId {}", cmd.cartId)
      raise(AddedItem(cmd.id, cmd.cartId, cmd.item))
    }
    case _ => log.debug("unknown event")
  }

}

